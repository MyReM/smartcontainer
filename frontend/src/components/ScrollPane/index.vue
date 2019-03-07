<style>
	.scroll-wrapper{
		overflow:hidden;
	}
	.scroll-container button{
		width:34px;
		height:34px;
		border:none;
		background-color:#f3f3f4;
		position:absolute;
		outline:none;
	}
	.scroll-container button:hover{
		background-color:#fff;
	}
	nav{
		position:absolute;
		height:34px;
		width:100%;
		margin-left:3em;
		overflow:hidden;
	}
</style>

<template>
  <div class="scroll-container" ref="scrollContainer" @wheel.prevent="handleScroll" id="page-tab">
    <button style="left:0;" id="btnLast" @click="btnLast()">
      <i class="el-icon-arrow-left"></i>
    </button>
    <nav>
      <div class="scroll-wrapper" ref="scrollWrapper" :style="{left: left + 'px'}" id="content">
        <slot></slot>
      </div>
    </nav>
    <button style="right:0" id="btnNext" @click="btnNext()">
      <i class="el-icon-arrow-right"></i>
    </button>
  </div>
</template>
<script>
import _$ from 'jquery'

const padding = 15 // tag's padding

export default {
  name: 'scrollPane',
  data() {
    return {
      left: 0
    }
  },
  methods: {
    handleScroll(e) {
      const eventDelta = e.wheelDelta || -e.deltaY * 3
      const $container = this.$refs.scrollContainer
      const $containerWidth = $container.offsetWidth
      const $wrapper = this.$refs.scrollWrapper
      const $wrapperWidth = $wrapper.offsetWidth

      if (eventDelta > 0) {
        this.left = Math.min(0, this.left + eventDelta)
      } else {
        if ($containerWidth - padding < $wrapperWidth) {
          if (this.left < -($wrapperWidth - $containerWidth + padding)) {
            this.left = this.left
          } else {
            this.left = Math.max(this.left + eventDelta, $containerWidth - $wrapperWidth - padding)
          }
        } else {
          this.left = 0
        }
      }
    },
    btnLast() {
      const nav = _$('#content')
      const left = parseInt(nav.css('margin-left'))
      if (left !== 0) {
        nav.animate({ 'margin-left': (left + 500 > 0 ? 0 : (left + 500)) + 'px' }, 150)
      }
    },
    btnNext() {
      const nav = _$('#content')
      const left = parseInt(nav.css('margin-left'))
      const wwidth = parseInt(_$('#page-tab').width())
      const navwidth = parseInt(nav.width())
      const allshowleft = -(navwidth - wwidth + 90)
      if (allshowleft !== left && navwidth > wwidth - 90) {
        const temp = (left - 500)
        nav.animate({ 'margin-left': (temp < allshowleft ? allshowleft : temp) + 'px' }, 150)
      }
    },
    moveToTarget($target) {
      const $container = this.$refs.scrollContainer
      const $containerWidth = $container.offsetWidth
      const $targetLeft = $target.offsetLeft
      const $targetWidth = $target.offsetWidth

      if ($targetLeft < -this.left) {
        // tag in the left
        this.left = -$targetLeft + padding
      } else if ($targetLeft + padding > -this.left && $targetLeft + $targetWidth < -this.left + $containerWidth - padding) {
        // tag in the current view
        // eslint-disable-line
      } else {
        // tag in the right
        this.left = -($targetLeft - ($containerWidth - $targetWidth) + padding)
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;
  .scroll-wrapper {
    position: absolute;
  }
}
</style>
