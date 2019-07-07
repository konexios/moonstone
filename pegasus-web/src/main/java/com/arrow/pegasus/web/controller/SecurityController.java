package com.arrow.pegasus.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.webapi.SecurityApiAbstract;

@RestController
@RequestMapping("/api/v1/core/security")
public class SecurityController extends SecurityApiAbstract {
}
