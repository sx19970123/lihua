package com.lihua.system.entity.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({MenuDirectoryValidation.class,MenuLinkValidation.class,MenuPageValidation.class,MenuPermsValidation.class})
public interface MenuValidation {
}
