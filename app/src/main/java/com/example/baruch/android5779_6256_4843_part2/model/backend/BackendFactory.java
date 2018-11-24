package com.example.baruch.android5779_6256_4843_part2.model.backend;

import com.example.baruch.android5779_6256_4843_part2.model.datasource.Firebase_DBManager;

public class BackendFactory {
    public static Backend getBackend(){
        return Firebase_DBManager.getFirebase_dbManager();
    }
}
