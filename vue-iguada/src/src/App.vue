<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "App",
  components: {},
  methods: {
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

.clearfix::before,
.clearfix::after {
  content: "";
  display: table;
  clear: both;
}
.el-table .row{
    background: #F1F7FF;
}
</style>
