import { next } from '../.././scripts/support'

export default {
  props: {
    initTriggerArray: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      triggerArray: this.initTriggerArray
    }
  },
  methods: {
    generateId(item) {
      item.triggerName = next()
    },
    removeId(item, index) {
      this.remove(index)
      if (this.triggerArray.length === 0) {
        this.add()
      }
    },
    put(staticObj) {
      this.triggerArray.push(Object.assign({}, staticObj))
    },
    remove(index) {
      this.triggerArray.splice(index, 1)
    }
  }
}
