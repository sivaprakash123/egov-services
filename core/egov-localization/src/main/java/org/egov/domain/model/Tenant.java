package org.egov.domain.model;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Tenant {

    public static final String DEFAULT_TENANT = "default";
    private static final String NAMESPACE_SEPARATOR = ".";
    private String tenantId;

    public List<String> getTenantHierarchy() {
        final ArrayList<String> tenantHierarchy = new ArrayList<>();
        final int tenantDepth = StringUtils.countMatches(tenantId, NAMESPACE_SEPARATOR);
        tenantHierarchy.add(tenantId);
        for (int index = tenantDepth; index >= 1; index--) {
            final String tenant =
                tenantId.substring(0, StringUtils.ordinalIndexOf(tenantId, NAMESPACE_SEPARATOR, index));
            tenantHierarchy.add(tenant);
        }
        tenantHierarchy.add(DEFAULT_TENANT);
        return tenantHierarchy;
    }
}
