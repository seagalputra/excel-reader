package com.seagalputra.excelreader;

import org.springframework.web.multipart.MultipartFile;

public interface UserAccountService {
    void batchInsert(MultipartFile multipartFile);
}
