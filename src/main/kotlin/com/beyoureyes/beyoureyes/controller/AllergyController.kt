package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.service.AllergyService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/update")
class AllergyController (
    private val allergyService: AllergyService
){

    @PatchMapping("/allergy")
    fun updateAllergyInfo(
        @RequestBody allergyMap: Map<String, Boolean>
    ): ResponseEntity<ResponseDto<Any?>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.principal as? Long
            ?: return ResponseEntity.status(401).body(ResponseUtil.error("인증되지 않은 사용자입니다.", null))

        return if (allergyService.updateAllergyInfo(userId, allergyMap)) {
            ResponseEntity.ok(ResponseUtil.success("알러지 정보가 업데이트 되었습니다.", null))
        } else {
            ResponseEntity.ok(ResponseUtil.error("알러지 정보 업데이트 실패했습니다.", null))
        }
    }


    @GetMapping("/allergy")
    fun getAllAllergy(): ResponseEntity<ResponseDto<Any>> {
        val data = allergyService.getAllAllergy()
        return ResponseEntity.ok(ResponseUtil.success("모든 알러지 정보 조회 성공", data))
    }
}