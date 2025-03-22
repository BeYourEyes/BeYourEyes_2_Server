package com.beyoureyes.beyoureyes.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import com.google.cloud.storage.Bucket
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.io.IOException

@Configuration
class FirebaseConfig {

    @Value("\${firebase.configuration-file}")
    private lateinit var account: String

    @Value("\${firebase.bucket}")
    private lateinit var bucket: String

    @Bean
    @Throws(IOException::class)
    fun firebaseApp(): FirebaseApp {
        val inputStream = FileInputStream(account)  // ⭐ 경로에서 직접 읽도록 수정

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(inputStream))
            .setStorageBucket(bucket)
            .build()

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    @Throws(IOException::class)
    fun bucket(): Bucket {
        return StorageClient.getInstance(firebaseApp()).bucket()
    }
}
