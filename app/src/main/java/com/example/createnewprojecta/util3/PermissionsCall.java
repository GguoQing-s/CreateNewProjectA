package com.example.createnewprojecta.util3;

import java.util.List;

public interface PermissionsCall {
    void errorRequest(String errorMsg);

    void granted();

    void denideList(List<String> list);
}
