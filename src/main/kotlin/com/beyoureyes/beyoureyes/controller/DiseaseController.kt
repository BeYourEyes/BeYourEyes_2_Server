package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.service.DiseaseService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/update/disease")
class DiseaseController (
    private val diseaseService: DiseaseService
) {
    @PatchMapping
    fun updateDisaseInfo(@RequestBody diseaseMap: Map<String, Boolean>) : ResponseEntity<ResponseDto<Unit>> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long

        return if (diseaseService.updateDiseaseInfo(userId, diseaseMap)) {
            ResponseEntity.ok(ResponseUtil.success("질환 정보가 업데이트 되었습니다." ,Unit))

        } else {
            ResponseEntity.ok(ResponseUtil.error("질환 정보 업데이트에 실패했습니다.", Unit))
        }
    }

    @GetMapping("/disease")
    fun getAllDisease(): ResponseEntity<ResponseDto<Any>> {
        val data = diseaseService.getAllDisease()
        return ResponseEntity.ok(ResponseUtil.success("모든 질환 정보 조회 성공", data))
    }
}