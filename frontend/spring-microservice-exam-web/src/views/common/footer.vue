<template>
  <div class="footer">
    <div class="container">
      <div class="site-info">
        <el-row>
          <el-col :span="3" :offset="2" class="footer-col">
            <h4>链接</h4>
            <a target="_blank" href="https://github.com/Breeze-P/ExamSystem">GITHUB</a>
            <a target="_blank" href="http://localhost:8081">管理后台</a>
            <a target="_blank" href="">更新日志</a>
          </el-col>
          <el-col :span="3" class="footer-col">
            <h4>工具</h4>
            <a target="_blank" href="http://element-cn.eleme.io">Element Ui</a>
            <a target="_blank" href="https://cn.vuejs.org/">Vue</a>
          </el-col>
          <el-col :span="3" class="footer-col">
            <h4>社区</h4>
            <a target="_blank" href="https://github.com/Breeze-P/ExamSystem">开发社区</a>
          </el-col>
        </el-row>
      </div>

      <div class="line"></div>

      <el-row :gutter="20" style="margin-top: 50px;">
        <el-col :span="12" :offset="8">
          <div class="copyright">
            <h4 class="content-c2" style="text-align: center;">Copyright ©2019</h4>
            <ul class="privacy">
              <li class="content-c1"><a class="content-c0" @click="openLayer">法律声明</a></li>
              <li class="content-c1"><a class="content-c0" @click="openPrivacy">隐私条款</a></li>
              <li class="content-c1"><a class="content-c0" target="_blank" href="https://github.com/Breeze-P/ExamSystem">开发者中心</a></li>
            </ul>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12" :offset="6">
          <div class="cop">
            <!-- <a class="content-c3" href="https://gitee.com/wells2333" target="_blank">
              <span class="content-c3">粤ICP备18038322号</span>
              <span class="content-c3">粤ICP备18038322号-1</span>
            </a> -->
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
export default {
  data () {
    return {
    }
  },
  methods: {
    openLayer () {
      this.$notify.info({
        title: '法律声明',
        message: '此仅为服务外包参赛项目，仅供学习参考，承担不起任何法律问题',
        offset: 70
      })
    },
    openPrivacy () {
      this.$notify.info({
        title: '隐私条款',
        message: '公正、公开、公正地不保证信息不被他人盗取和使用',
        offset: 70
      })
    }
  },
  components: {}
}
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @font-face {
    font-family: "HelloTeamfont";
    src: url("../../../static/font/ZCOOLXiaoWei-Regular.ttf");
  }
  .footer {
    background-position: 50%;
    background-color: #252525;
    width: 100%;
    box-sizing: border-box;
    height: 400px;

    font-family: HelloTeamfont;
  }
  .container {
    width: 1150px;
    margin: 0 auto;
    position: relative;
    z-index: 1;
  }
  .c5 {
    color: #646464;
    right: -70px;
    position: relative;
  }

  .copyright {
    color: #434d55;
    font-size: 12px;
    padding: 20px 0 0;
    display: flex;
  }

  .privacy {
    float: left;
    margin: 0 0 0 12px;
  }

  .content-c0 {
    color: #5079d9;
    cursor: pointer;
    text-decoration: none;
    &:hover {
      color: #3A5FCD;
    }
  }

  .content-c1 {
    float: left;
    line-height: 12px;
    padding: 1px 10px 0;
    border-left: 1px solid #ccc;
    list-style: none;
  }

  .content-c2 {
    float: left;
    height: 15px;
    line-height: 15px;
    color: #ffffffa6;
    margin: 0 0 0 12px;
  }

  .cop {
    text-align: center;
    height: 15px;
  }

  .content-c3 {
    margin-right: 20px;
    color: #ffffffa6;
    font-size: 12px;
    height: 12px;
    line-height: 1;
  }

  .footer-col {
    width: 200px;
    float: left;
    margin-top: 30px;
    h4 {
      color: #fff;
      font-size: 30px;
      font-weight: 400;
    }
    a {
      display: block;
      margin: 0;
      line-height: 2.5;
      font-size: 20px;
      font-weight: 400;
      color: #ffffffa6;
    }
  }
</style>
