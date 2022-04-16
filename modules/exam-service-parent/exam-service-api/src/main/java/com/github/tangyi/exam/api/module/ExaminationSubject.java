package com.github.tangyi.exam.api.module;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tangyi.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 考试题目关联
 *
 * @author zdz
 * @date 2022/04/16 14:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationSubject extends BaseEntity<ExaminationSubject> {

    /**
     * 考试ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long examinationId;

    /**
     * 分类ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;

    /**
     * 题目ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long subjectId;

    /**
     * 题目类型，0：选择题，1：简答题，2：判断题，3：多选题
     */
    private Integer type;

}
