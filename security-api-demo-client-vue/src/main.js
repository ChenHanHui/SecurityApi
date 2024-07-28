import { createApp } from 'vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

// import '@/assets/styles/index.scss' // global css

import App from './App.vue'

// 注册指令
import plugins from './plugins' // plugins

import { download } from '@/utils/request'
import { parseTime, resetForm } from '@/utils'

const app = createApp(App)

// 全局方法挂载
app.config.globalProperties.download = download
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm

app.use(plugins)

// 使用element-plus 并且设置全局的大小
app.use(ElementPlus, {
  locale: locale,
  // 支持 large、default、small
  size: 'default'
})

app.mount('#app')
