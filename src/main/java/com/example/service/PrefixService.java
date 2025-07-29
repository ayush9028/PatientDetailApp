package com.example.service;

import com.example.entity.Prefix;
import com.example.helper.PrefixHelper;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RemoteProxy(name = "prefixService")
@Service("prefixService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrefixService {

    @Autowired
    private PrefixHelper helper;

    @RemoteMethod
    @Transactional
    public String[] create(Prefix prefix) {
        try {
            helper.save(prefix);  // ✅ Using helper layer
            return new String[]{"success", "Saved successfully"};
        } catch (Exception ex) {
            ex.printStackTrace();
            return new String[]{"error", "Error while saving Prefix"};
        }
    }

    @RemoteMethod
    @Transactional
    public String[] delete(int id) {
        try {
            helper.deleteById(id);  // ✅ Using helper layer
            return new String[]{"success", "Deleted successfully"};
        } catch (Exception ex) {
            ex.printStackTrace();
            return new String[]{"error", "Error while deleting Prefix"};
        }
    }



    @RemoteMethod
    public Prefix[] list() {
        List<Prefix> list = helper.fetchAll();
        return list.toArray(new Prefix[0]);
    }
}

