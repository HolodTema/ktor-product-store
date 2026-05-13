package com.terabyte.service

import com.terabyte.model.JobTitle
import com.terabyte.model.JobTitles
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class JobTitleService {

    fun getJobTitleById(id: Int): JobTitle? = transaction {
        JobTitles.select { JobTitles.id eq id }
            .map { row ->
                JobTitle(
                    id = row[JobTitles.id],
                    name = row[JobTitles.name],
                    isAdmin = row[JobTitles.isAdmin],
                )
            }.singleOrNull()
    }

}
