/**
 *  ---- OPTION MESSAGE ----
 *  OptionMessage is the response dto of the available options
 *  available for a particular optionType (USER, GROUP)
 *  Again, this is only a Client Side DTO and not the actual
 *  03/13/2020
 */
package com.srijan.pandey.client.model.response;

public class OptionMessage extends AbstractClientResponse {

    private int code;
    private String value;
    private String optionType;

    public OptionMessage(int code, String value, String optionType) {
        this.code = code;
        this.value = value;
        this.optionType = optionType;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getMessage() {
        return String.format("%s - %s", value, optionType);
    }

}
