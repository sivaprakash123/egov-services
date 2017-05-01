package org.egov.domain.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TenantTest {

    @Test
    public void test_should_split_name_spaced_tenant_code_to_hierarchy_list() {
        final Tenant tenant = new Tenant("a.b.c.d");

        final List<String> tenantHierarchyList = tenant.getTenantHierarchy();

        assertEquals(5, tenantHierarchyList.size());
        assertEquals("a.b.c.d", tenantHierarchyList.get(0));
        assertEquals("a.b.c", tenantHierarchyList.get(1));
        assertEquals("a.b", tenantHierarchyList.get(2));
        assertEquals("a", tenantHierarchyList.get(3));
        assertEquals("default", tenantHierarchyList.get(4));
    }

    @Test
    public void test_should_return_default_tenant_along_with_tenant_id_when_namespace_not_present() {
        final Tenant tenant = new Tenant("a");

        final List<String> tenantHierarchyList = tenant.getTenantHierarchy();

        assertEquals(2, tenantHierarchyList.size());
        assertEquals("a", tenantHierarchyList.get(0));
        assertEquals("default", tenantHierarchyList.get(1));
    }

}