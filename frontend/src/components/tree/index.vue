<template>
  <div class="tree-container">

    <el-tabs type="border-card">
      <el-tab-pane label="角色管理">
        
        <el-input placeholder="输入关键字进行过滤" v-model="filterText" style="margin-bottom: 15px"></el-input>

        <el-tree :data="roleTreeData" node-key="value" default-expand-all :expand-on-click-node="true" @node-click="roleNodeClick">
          <span class="tree-node" slot-scope="{ node, data }">
            <span><i class="el-icon-document" style="color: bule"></i> {{ node.label }}</span>
          </span>
        </el-tree>
      </el-tab-pane>

      <el-tab-pane label="组织架构">

        <el-input placeholder="输入关键字进行过滤" v-model="filterText" style="margin-bottom: 15px"></el-input>

        <el-tree :data="deptTreeData" node-key="value" default-expand-all :expand-on-click-node="true" @node-click="deptNodeClick">
          <span class="tree-node" slot-scope="{ node, data }">
            <span><i class="el-icon-document" style="color: bule"></i> {{ node.label }}</span>
          </span>
        </el-tree>
      </el-tab-pane>
    </el-tabs>

</div>
</template>

<script>

export default {
  name: 'tree',
  props: [
    'filterText',
    'deptTreeData',
    'roleTreeData'
  ],

  methods: {
    roleNodeClick(ele, node, tree) {
      this.$emit('role-node-click', { 'ele': ele, 'node': node, 'tree': tree })
    },
    deptNodeClick(ele, node, tree) {
      this.$emit('dept-node-click', { 'ele': ele, 'node': node, 'tree': tree })
    }
  }
}
</script>

<style>
  .tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>