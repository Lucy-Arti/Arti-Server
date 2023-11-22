package com.lucy.arti.designer.repository;

import com.lucy.arti.designer.domain.Designer;
import com.lucy.arti.global.exception.BusinessException;
import com.lucy.arti.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository extends JpaRepository<Designer, Long> {

    default Designer findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
            () -> BusinessException.from(ErrorCode.DESIGNER_NOT_FOUND));
    }
}
