/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author host
 */
@Getter
@Setter
public class MailDto {
    private int count = 0;
    private String subject;
    private String reference;
    private String body;
    private String date;
}
