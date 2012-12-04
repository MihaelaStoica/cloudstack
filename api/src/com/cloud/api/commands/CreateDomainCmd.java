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

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.DomainResponse;
import com.cloud.domain.Domain;
import com.cloud.user.Account;
import com.cloud.user.UserContext;

@Implementation(description="Creates a domain", responseObject=DomainResponse.class)
public class CreateDomainCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(CreateDomainCmd.class.getName());

    private static final String s_name = "createdomainresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, required=true, description="creates domain with this name")
    private String domainName;

    @IdentityMapper(entityTableName="domain")
    @Parameter(name=ApiConstants.PARENT_DOMAIN_ID, type=CommandType.LONG, description="assigns new domain a parent domain by domain ID of the parent.  If no parent domain is specied, the ROOT domain is assumed.")
    private Long parentDomainId;

    @Parameter(name=ApiConstants.NETWORK_DOMAIN, type=CommandType.STRING, description="Network domain for networks in the domain")
    private String networkDomain;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getDomainName() {
        return domainName;
    }

    public Long getParentDomainId() {
        return parentDomainId;
    }

    public String getNetworkDomain() {
        return networkDomain;
    }  

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }
    
    @Override
    public void execute(){
        UserContext.current().setEventDetails("Domain Name: "+getDomainName()+((getParentDomainId()!=null)?", Parent DomainId :"+getParentDomainId():""));
        Domain domain = _domainService.createDomain(getDomainName(), getParentDomainId(), getNetworkDomain());
        if (domain != null) {
            DomainResponse response = _responseGenerator.createDomainResponse(domain);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to create domain");
        }
    }
}
