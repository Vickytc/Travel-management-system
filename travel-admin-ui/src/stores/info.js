import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('info', () => {
      const info = ref('')
      const setInfo=(newInfo)=>{
        info.value=newInfo;
      }
      const removeInfo=()=>{
        info.value=''
      }
      return { info,setInfo,removeInfo }
    },
    {
      persist:true
    }
)
