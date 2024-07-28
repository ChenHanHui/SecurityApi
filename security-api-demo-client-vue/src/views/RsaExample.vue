<template>
  <div style="width: 100%; margin: auto;">
    <el-form ref="formRef" style="max-width: 250px; margin: auto;" :model="form" :rules="rules" label-width="auto" :size="formSize">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input-number v-model="form.age" />
      </el-form-item>
      <el-form-item label="日期" prop="day">
        <el-date-picker v-model="form.day" type="date" value-format="YYYY-MM-DD" placeholder="日期" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="resetForm()">重置</el-button>
      </el-form-item>
    </el-form>
    <div v-if="getResponse">get方法: {{ getResponse }}</div>
    <div v-if="outEncodeResponse">outEncode方法: {{ outEncodeResponse }}</div>
    <div v-if="inDecodeOutEncodeResponse">inDecodeOutEncode方法: {{ inDecodeOutEncodeResponse }}</div>
    <div v-if="inDecodeResponse">inDecode方法: {{ inDecodeResponse }}</div>
  </div>
</template>

<script setup>
import { reactive, ref, toRefs, getCurrentInstance } from 'vue'
import server from '@/api/author'
import { parseTime } from '@/utils'

const { proxy } = getCurrentInstance()

const formSize = ref('default')

const data = reactive({
  form: {
    name: '一碗情深',
    age: 27,
    day: parseTime(new Date(), '{y}-{m}-{d}')
  },
  rules: {
    name: [{ required: true, message: '请输入名称', trigger: 'change' }],
    age: [{ required: true, message: '请输入年龄', trigger: 'change' }],
    day: [{ required: true, message: '请选择日期', trigger: 'change' }]
  }
})
const { form, rules } = toRefs(data)

const getResponse = ref()
const outEncodeResponse = ref()
const inDecodeResponse = ref()
const inDecodeOutEncodeResponse = ref()

/** 提交按钮 */
function submitForm() {
  console.log(form.value)
  proxy.$refs['formRef'].validate((valid, fields) => {
    if (valid) {
      console.log('submit!')

      server.get(form.value).then(res => {
        proxy.$modal.msgSuccess("get方法提交成功")
        getResponse.value = res.data
      })

      server.outEncode(form.value).then(res => {
        proxy.$modal.msgSuccess("outEncode方法提交成功")
        outEncodeResponse.value = res.data
      })

      server.inDecodeOutEncode(form.value).then(res => {
        proxy.$modal.msgSuccess("inDecodeOutEncode方法提交成功")
        inDecodeOutEncodeResponse.value = res.data
      })

      server.inDecode(form.value).then(res => {
        proxy.$modal.msgSuccess("inDecode方法提交成功")
        inDecodeResponse.value = res.data
      })
    } else {
      console.log('error submit!', fields)
    }
  })
}

/** 重置操作表单 */
function resetForm() {
  form.value = {}
  proxy.resetForm('formRef')
}

</script>
