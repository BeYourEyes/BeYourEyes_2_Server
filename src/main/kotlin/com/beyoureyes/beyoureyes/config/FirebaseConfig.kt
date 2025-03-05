package com.beyoureyes.beyoureyes.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import com.google.cloud.storage.Bucket
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
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
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(ClassPathResource(account).inputStream))
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