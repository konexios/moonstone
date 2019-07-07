package com.arrow.pegasus.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "pegasusAccessKeyApi")
@RequestMapping("/api/v1/pegasus/access-keys")
public class AccessKeyApi extends BaseApiAbstract {
}
