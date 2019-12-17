package com.appachhi.sdk.database.dao;

public interface APICallDao {
    public long[] insertApiCalls(APICallEntity... apiCallEntities);

    public long insertApiCall(APICallEntity apiCallEntity);

    // "SELECT * FROM api_call where syncStatus = 0 AND session_id in (:sessionIds) limit 100"
    List<APICallEntity> allUnSyncedApiCallEntityForSession(List<String> sessionIds);

    // "UPDATE api_call SET  syncStatus = 1 WHERE id IN (:ids)"
    void updateSuccessSyncStatus(List<String> ids);
  
    // "SELECT * FROM api_call"
    public List<APICallEntity> allApiCalls();

    @Delete()
    public void deleteApiCalls(APICallEntity apiCallEntity);
}
