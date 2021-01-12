package com.seagalputra.excelreader;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Row;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    private final ExcelReader excelReader;

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    @Override
    public void batchInsert(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            var userAccounts = excelReader.read(inputStream, this::mapToUserAccount);

            var partitions = splitUserAccounts(userAccounts, 5);

            var futures = new CompletableFuture[partitions.size()];
            for (int i = 0; i < partitions.size(); i++) {
                futures[i] = executeBatch(partitions.get(i));
            }

            CompletableFuture.allOf(futures);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CompletableFuture<Void> executeBatch(List<UserAccount> userAccounts) {
        return CompletableFuture.runAsync(() -> {
            userAccountRepository.saveAll(userAccounts);
        }, executor);
    }

    private List<List<UserAccount>> splitUserAccounts(List<UserAccount> userAccounts, int batchSize) {
        List<List<UserAccount>> batchUserAccounts = new ArrayList<>();
        int from = userAccounts.size() / batchSize;
        for (int i = 0; i < userAccounts.size(); i += from) {
            int to = i + from;
            List<UserAccount> subUserAccounts = userAccounts.subList(i, to);
            batchUserAccounts.add(subUserAccounts);
        }
        return batchUserAccounts;
    }

    private UserAccount mapToUserAccount(Row row) {
        return UserAccount.builder()
                .firstName(excelReader.getStringValue(row, 0))
                .lastName(excelReader.getStringValue(row, 1))
                .email(excelReader.getStringValue(row, 2))
                .gender(excelReader.getStringValue(row, 3))
                .jobTitle(excelReader.getStringValue(row, 4))
                .build();
    }
}
