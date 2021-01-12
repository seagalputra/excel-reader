package com.seagalputra.excelreader;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends PagingAndSortingRepository<UserAccount, Long> {
}
