package com.beyoureyes.beyoureyes.config

import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ResponseDto<Nothing?>> {
        e.printStackTrace()
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseUtil.error("서버 오류가 발생했습니다.", null))
    }
}