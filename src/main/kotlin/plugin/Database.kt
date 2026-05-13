package com.terabyte.plugin

import com.terabyte.model.Employees
import com.terabyte.model.JobTitles
import com.terabyte.model.ProductCategories
import com.terabyte.model.Products
import com.terabyte.model.Receipts
import com.terabyte.model.SaleOperations
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val url = environment.config.property("database.url").getString()
    val driver = environment.config.property("database.driverClassName").getString()
    val maxPoolSize = environment.config.property("database.maxPoolSize").getString().toInt()

    val hikariConfig = HikariConfig().apply {
        jdbcUrl = url
        driverClassName = driver
        maximumPoolSize = maxPoolSize
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    val dataSource = HikariDataSource(hikariConfig)

    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(
            Employees,
            JobTitles,
            Products,
            ProductCategories,
            Receipts,
            SaleOperations
        )
    }
}