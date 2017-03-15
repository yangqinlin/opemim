package com.shinemo.contacts.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shinemo.openim.utils.Jsons;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by yangqinlin on 15/12/30.
 */
public class ContactHelper {

    private TreeMap<Long, Long> mVersionMap;
    private Map<Long, Long> mUserVersionMap;
    private Map<Long, Long> mDirectVersionMap;//直接公司下人员变动版本号

    public static ContactHelper INSTANCE = instance();

    private ContactHelper() {
    }

    public static ContactHelper instance() {
        if (INSTANCE == null) {
            synchronized (ContactHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ContactHelper();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized TreeMap<Long, Long> getVerMap(String keyVal) {
        TreeMap<Long, Long> latestMap = new TreeMap<>();
        try {
            String orgVersion = CoreSPManager.getInstance().getString(keyVal);
            if (!TextUtils.isEmpty(orgVersion)) {
                latestMap = Jsons.fromJson(orgVersion,
                        new TypeToken<TreeMap<Long, Long>>() {
                        }.getType());

                //以下是Gson转换特殊处理,部分机型会报ClassCastException
                Iterator it = latestMap.keySet().iterator();
                TreeMap<Long, Long> treeMap = new TreeMap<>();
                while (it.hasNext()) {
                    Object key = it.next();
                    Object value = latestMap.get(key);
                    Long lKey, lValue;

                    if ((key instanceof Long)) {
                        lKey = (Long) key;
                    } else {
                        if (key instanceof Number) {
                            lKey = ((Number) key).longValue();
                        } else if (key instanceof String) {
                            lKey = Double.valueOf((String) key).longValue();
                        } else {
                            throw new IllegalArgumentException("Invalid key in TreeMap: "  + key);
                        }
                    }

                    if ((value instanceof Long)) {
                        lValue = (Long) value;
                    } else {
                        if (value instanceof Number) {
                            lValue = ((Number) value).longValue();
                        } else if (value instanceof String) {
                            lValue = Double.valueOf((String) value).longValue();
                        } else {
                            throw new IllegalArgumentException("Invalid value in TreeMap: " + value);
                        }
                    }
                    treeMap.put(lKey, lValue);
                }
                return treeMap;
            }
        } catch (Exception e) {
        }
        return latestMap;
    }

    public synchronized void setVerMap(String key, TreeMap<Long, Long> latestMap) {
        if(latestMap != null && latestMap.size() > 0) {
            CoreSPManager.getInstance().putString(key, Jsons.toJson(latestMap));
        }
    }


    public void updateOrgVerMap(final TreeMap<Long, Long> map) {
        if(map == null || map.size()==0) {
            CoreSPManager.getInstance().putString(
                    SharePrfConstant.ORG_VERSION, "");
            mVersionMap = null;
        } else {
            Gson gson = new Gson();
            CoreSPManager.getInstance().putString(
                    SharePrfConstant.ORG_VERSION, gson.toJson(map));
            mVersionMap = map;
        }
    }

    public TreeMap<Long, Long> getOrgVerMap() {
        if(mVersionMap == null) {
            mVersionMap = getVerMap(SharePrfConstant.ORG_VERSION);
        }
        return mVersionMap;
    }

    public Map<Long, Long> getUserVerMap() {
        if(mUserVersionMap == null) {
            mUserVersionMap = getVerMap(SharePrfConstant.ORG_USER_VERSION);
        }
        return mUserVersionMap;
    }

    public synchronized void setUserVerMap(Map<Long, Long> userVerMap) {
        if(userVerMap != null && userVerMap.size() > 0) {
            Gson gson = new Gson();
            CoreSPManager.getInstance().putString(
                    SharePrfConstant.ORG_USER_VERSION, gson.toJson(userVerMap));
            mUserVersionMap = userVerMap;
        } else {
            CoreSPManager.getInstance().putString(
                    SharePrfConstant.ORG_USER_VERSION, "");
            mUserVersionMap = null;
        }
    }

    public Map<Long, Long> getDirectVerMap() {
        if(mDirectVersionMap == null) {
            mDirectVersionMap = getVerMap(SharePrfConstant.CONTACTS_DIRECT_VERSION);
        }
        return mDirectVersionMap;
    }

    public void setDirectVerMap(Map<Long, Long> directVerMap) {
        if(directVerMap != null && directVerMap.size() > 0) {
            Gson gson = new Gson();
            CoreSPManager.getInstance().putString(
                    SharePrfConstant.CONTACTS_DIRECT_VERSION, gson.toJson(directVerMap));
            mDirectVersionMap = directVerMap;
        }
    }

    public void recycle(){
        mVersionMap = null;
        mUserVersionMap = null;
        mDirectVersionMap = null;
    }
}
