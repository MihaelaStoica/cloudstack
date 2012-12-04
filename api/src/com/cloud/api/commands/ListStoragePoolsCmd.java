// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.StoragePoolResponse;
import com.cloud.async.AsyncJob;
import com.cloud.storage.StoragePool;
import com.cloud.utils.Pair;

@Implementation(description="Lists storage pools.", responseObject=StoragePoolResponse.class)
public class ListStoragePoolsCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListStoragePoolsCmd.class.getName());

    private static final String s_name = "liststoragepoolsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="cluster")
    @Parameter(name=ApiConstants.CLUSTER_ID, type=CommandType.LONG, description="list storage pools belongig to the specific cluster")
    private Long clusterId;

    @Parameter(name=ApiConstants.IP_ADDRESS, type=CommandType.STRING, description="the IP address for the storage pool")
    private String ipAddress;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="the name of the storage pool")
    private String storagePoolName;

    @Parameter(name=ApiConstants.PATH, type=CommandType.STRING, description="the storage pool path")
    private String path;

    @IdentityMapper(entityTableName="host_pod_ref")
    @Parameter(name=ApiConstants.POD_ID, type=CommandType.LONG, description="the Pod ID for the storage pool")
    private Long podId;

    @IdentityMapper(entityTableName="data_center")
    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.LONG, description="the Zone ID for the storage pool")
    private Long zoneId;
    
    @IdentityMapper(entityTableName="storage_pool")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="the ID of the storage pool")
    private Long id;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getClusterId() {
        return clusterId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public String getPath() {
        return path;
    }

    public Long getPodId() {
        return podId;
    }

    public Long getZoneId() {
        return zoneId;
    }
    
    public Long getId() {
    	return id;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }
    
    public AsyncJob.Type getInstanceType() {
        return AsyncJob.Type.StoragePool;
    }

    @Override
    public void execute(){
        Pair<List<? extends StoragePool>, Integer> pools = _mgr.searchForStoragePools(this);
        ListResponse<StoragePoolResponse> response = new ListResponse<StoragePoolResponse>();
        List<StoragePoolResponse> poolResponses = new ArrayList<StoragePoolResponse>();
        for (StoragePool pool : pools.first()) {
            StoragePoolResponse poolResponse = _responseGenerator.createStoragePoolResponse(pool);
            poolResponse.setObjectName("storagepool");
            poolResponses.add(poolResponse);
        }

        response.setResponses(poolResponses, pools.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
