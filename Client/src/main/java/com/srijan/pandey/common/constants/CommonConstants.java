
/**
 * --- MessageType---
 * Some Constants that is used by the
 * Server and Client
 * 02/30/2020
 */
package com.srijan.pandey.common.constants;

public class CommonConstants {

    public interface MessageDelimiters {
        String MESSAGE_START = "<<";
        String MESSAGE_END = ">>";
        String MESSAGE_SPLIT = "|";
        String MESSAGE_SPLIT_REGEX = "\\|";
    }

    public interface ModeSelectionConstants {
        String USER ="U";
        String GROUP = "G";
    }

    public interface MessageSize {
        int REQUEST_MESSAGE_SIZE = 256;
    }
}
