<template>
  <div class="content-container">
    <h1 :style="`margin: 30px`">考试中心</h1>
    <div class="exam-card-list">
      <transition name="fade-transform" mode="out-in" v-for="exam in examList" :key="exam.id">
        <div style="cursor: pointer" class="card-item" v-show="exam.show" @click="startExam(exam)">
          <div class="card-item-detail">
            <div>
              <a href="javascript:void(-1);" @click="startExam(exam)"></a>
              <h3>
                <div class="card-item-name mb-12">
                  {{ exam.examinationName  | simpleStrFilter }}
                </div>
              </h3>
            </div>
            <div class="card-item-course" v-if="exam.course !== undefined && exam.course !== null">
              <div class="card-item-course-detail mb-12">
                <span @click.stop="handleComeIntoCourse(exam.course.id)">{{ exam.course.courseName }}</span>
              </div>
              <div class="card-item-course-detail mb-12">
                <span>{{ exam.startTime | timeFilter }}~{{ exam.endTime | timeFilter }}</span>
              </div>
            </div>
          </div>
        </div>
      </transition>
      <!-- 对齐 -->
      <i v-if="examList !== undefined && examList.length > 0" v-for="count in (examList.length)" :key="count"></i>
    </div>
    <el-row style="text-align: center; margin-bottom: 50px;">
      <el-col :span="24">
        <el-button type="default" @click="scrollList" :loading="loading" style="margin-bottom: 100px;">加载更多</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {mapGetters, mapState} from 'vuex'
import { fetchList } from '@/api/exam/exam'
import { getCurrentTime } from '@/api/exam/examRecord'
import { isNotEmpty, messageFail, messageWarn, getAttachmentPreviewUrl, formatDate } from '@/utils/util'
import store from '@/store'
import moment from 'moment'
import PanThumb from '@/components/PanThumb'

export default {
  components: { PanThumb },
  filters: {
    examTypeFilter (type) {
      const typeMap = {
        0: '正式考试',
        1: '模拟考试',
        2: '在线练习',
        3: '调查问卷'
      }
      return typeMap[type]
    },
    timeFilter (time) {
      return formatDate(new Date(time), 'yyyy-MM-dd hh:mm')
    }
  },
  data () {
    return {
      total: 0,
      loading: true,
      examList: [],
      isLastPage: false,
      query: {
        sort: 'id',
        order: ' asc',
        pageNum: 1,
        pageSize: 6,
        examinationName: '',
        status: 0
      },
      tempExamRecord: {
        id: null,
        userId: null,
        examinationId: null
      },
      // 默认全部
      activeTag: '1'
    }
  },
  computed: {
    // 获取用户信息
    ...mapState({
      userInfo: state => state.user.userInfo,
      course: state => state.course.course,
      sysConfig: state => state.sysConfig.sysConfig,
      examRecord: state => state.exam.examRecord
    }),
    ...mapGetters([
      'subject'
    ])
  },
  created () {
    if (isNotEmpty(this.course)) {
      this.query.courseId = this.course.id
    }
    if (this.$route.query.query !== '') {
      this.query.examinationName = this.$route.query.query
    }
    this.query.pageNum = 1
    // 加载考试列表
    this.getExamList()
  },
  methods: {
    // 加载考试列表
    getExamList () {
      this.loading = true
      fetchList(this.query).then(response => {
        const { total, isLastPage, list } = response.data
        this.total = total
        this.isLastPage = isLastPage
        this.updateExamList(list)
        this.loading = false
      }).catch(() => {
        messageWarn(this, '加载考试失败！')
        this.loading = false
      })
    },
    // 列表滚动
    scrollList () {
      if (this.isLastPage) {
        messageWarn(this, '暂无更多数据！')
        return
      }
      if (this.loading) {
        messageWarn(this, '正在拼命加载！')
        return
      }
      this.loading = true
      setTimeout(() => {
        this.query.pageNum++
        fetchList(this.query).then(response => {
          const { total, isLastPage, list } = response.data
          this.updateExamList(list)
          this.total = total
          this.isLastPage = isLastPage
          this.loading = false
        }).catch(() => {
          messageWarn(this, '加载考试失败！')
        })
      }, 1000)
    },
    // 开始考试
    startExam (exam) {
      this.tempExamRecord.examinationId = exam.id
      this.tempExamRecord.userId = this.userInfo.id
      getCurrentTime().then(response => {
        // 校验考试时间
        const currentTime = moment(response.data.data)
        // 校验结束时间
        if (currentTime.isAfter(exam.endTime)) {
          messageWarn(this, '考试已结束')
        } else if (currentTime.isBefore(exam.startTime)) {
          // 考试未开始
          messageWarn(this, '考试未开始')
        } else {
          this.$confirm('确定要开始吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            // 开始考试
            store.dispatch('StartExam', this.tempExamRecord).then(() => {
              if (this.examRecord === undefined || this.subject === undefined) {
                messageWarn(this, '开始考试失败')
                return
              }
              this.$router.push({ path: `/start/${exam.id}-${this.examRecord.id}-${this.subject.id}-${this.subject.type}` })
            }).catch(() => {
              messageWarn(this, '开始考试失败')
            })
          }).catch(() => {
            console.log('取消考试')
          })
        }
      }).catch(() => {
        messageFail(this, '开始考试失败！')
      })
    },
    getAvatar (avatar) {
      return getAttachmentPreviewUrl(this.sysConfig, avatar)
    },
    submitForm () {
      this.query.pageNum = 1
      this.getExamList()
    },
    resetForm () {
      this.query.examinationName = ''
    },
    // 切换tag
    changeTag (tag) {
      this.activeTag = tag
      if (tag === '2') {
        this.query.sort = 'create_date'
      } else if (tag === '3') {
        this.query.sort = 'create_date'
      } else {
        this.query.sort = 'id'
      }
      this.getExamList()
    },
    handleSizeChange (val) {
      this.query.limit = val
      this.getExamList()
    },
    handleCurrentChange (val) {
      this.query.pageNum = val
      this.getExamList()
    },
    updateExamList (list) {
      if (list === undefined || list.length === 0) {
        return list
      }
      list.forEach(item => {
        item.show = false
      })
      if (this.examList.length === 0) {
        this.examList = list
      } else {
        list.forEach(item => {
          let exist = false
          for (let i = 0; i < this.examList.length; i++) {
            if (this.examList[i].id === item.id) {
              exist = true
            }
          }
          if (!exist) {
            this.examList.push(item)
          }
        })
      }
      for (let i = 0; i < list.length; i++) {
        setTimeout(() => {
          list[i].show = true
        }, 250 + (100 * i))
      }
    },
    handleComeIntoCourse (id) {
      this.$router.push({name: 'course-details', query: {courseId: id}})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" rel="stylesheet/scss" scoped>
  .exam-empty {
    font-size: 13px;
    color: #999;
    text-align: center;
  }
  .category-list {
    margin: 0 auto 30px;
    padding: 15px 10px;
    width: 98%;
    box-shadow: 0 5px 15px 0 rgba(82,94,102,.1);
    border-radius: 4px;
    ul {
      margin: 0;
      overflow: hidden;
    }
    .active {
      color: #fff;
      background: #409eff;
    }
    li  {
      list-style: none;
      display: block;
      float: left;
      margin: 10px;
      padding: 0 15px;
      height: 24px;
      line-height: 24px;
      color: #666;
      font-size: 13px;
      border-radius: 5px;
      cursor: pointer;
    }
  }

  .exam-card-list {
    width: 100%;
    height: auto;
    overflow: auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    flex-direction: row;
    .card-item {
      width: 30%;
      position: relative;
      margin-bottom: 100px;
      border-radius: 6px;
      box-shadow: 0 3px 8px rgba(0, 0, 0, 0.15);
      .card-item-snapshoot {
        border: 1px solid rgba(0,0,0,.15);
        background-origin: border-box;
        background-size: cover;
        background-color: #f0f0f0;
        background-position: 49% 38% ;
        height: 172px;
        display: block;
        border-radius: 6px 6px 0 0;
      }
      .card-item-detail {
        padding: 20px;
        .card-item-name {
          overflow:hidden; //超出的文本隐藏
          text-overflow:ellipsis; //溢出用省略号显示
          white-space:nowrap; //溢出不换行
        }
        .card-item-course {
          --x-height-multiplier: 0.342;
          --baseline-multiplier: 0.22;
          font-weight: 300;
          font-style: normal;
          letter-spacing: 0;
          .card-item-course-detail {
            color: rgba(0,0,0,.54);
            fill: rgba(0,0,0,.54);
            span {
              color: rgba(0, 0, 0, 0.4);
              display: inline-block;
              font-size: 14px;
              font-weight: 400;
              margin-right: 10px;
              &:hover {
                color: #000;
              }
            }
          }
        }
      }
    }
    >i {
      width: 30%;
      padding-right: 12px;
    }
  }
</style>
