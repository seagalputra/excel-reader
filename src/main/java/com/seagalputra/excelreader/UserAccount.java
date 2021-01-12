package com.seagalputra.excelreader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserAccount {

    @Id
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;

    @Column("gender")
    private String gender;

    @Column("job_title")
    private String jobTitle;

    private UserAccount of(String firstName, String lastName, String email, String gender, String jobTitle) {
        return new UserAccount(null, firstName, lastName, email, gender, jobTitle);
    }
}
