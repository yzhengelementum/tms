package com.tms.model.datatype;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by yzheng on 8/31/16.
 */

@AllArgsConstructor
@Setter
@Getter
public class User {
    @NotNull
    private String userBid;
    private String userName;
    private List<ContactInfo> contacts;
}
