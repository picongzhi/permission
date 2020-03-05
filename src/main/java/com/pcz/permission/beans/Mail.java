package com.pcz.permission.beans;

import lombok.*;

import java.util.Set;

/**
 * @author picongzhi
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String subject;
    private String message;
    private Set<String> receivers;
}
