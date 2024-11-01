package com.spring.usermanagement.services;

import com.spring.usermanagement.model.dto.auth.OrgDto;

import java.util.Map;

public interface OrganizationService {

    OrgDto create(Map<String, Object> params);
}
