import { defineComponent } from 'vue';
import Icon from '@ant-design/icons-vue';
import i18n from './i18n.svg';

export default defineComponent({
  setup() {
    return () => <Icon type={i18n} />;
  },
});
