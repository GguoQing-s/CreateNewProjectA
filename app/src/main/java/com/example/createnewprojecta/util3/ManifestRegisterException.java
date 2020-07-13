package com.example.createnewprojecta.util3;

public class ManifestRegisterException extends RuntimeException {
    ManifestRegisterException(String permission) {
        super(permission == null ?
                "该权限没有在manifest中注册" :
                (permission + ": Permissions are not registered in the manifest file"));
    }
}
