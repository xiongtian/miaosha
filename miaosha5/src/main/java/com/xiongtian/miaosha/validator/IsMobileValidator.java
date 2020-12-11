package com.xiongtian.miaosha.validator;


import com.xiongtian.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author xiongtian
 * @Date 2020/12/3 10:58
 * @Version 1.0
 */

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {


    private boolean required = false;


    /**
     * 初始化方法
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(IsMobile constraintAnnotation) {

        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if (required) {
            return ValidatorUtil.isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
