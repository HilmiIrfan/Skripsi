/* eslint-disable react/prop-types */
import { useState } from 'react'
import { connect } from 'react-redux'
import { Drawer, Switch, Row, Col, Divider, Alert, Button } from 'antd'
import { toggleSettingPanel, changeSetting } from '@/store/actions'
import clip from '@/utils/clipboard'
import { NotificationOutlined } from '@ant-design/icons'

const RightPanel = (props) => {
  const {
    settingPanelVisible,
    toggleSettingPanel,
    changeSetting,
    sidebarLogo: defaultSidebarLogo,
    fixedHeader: defaultFixedHeader,
    tagsView: defaultTagsView,
  } = props

  const [sidebarLogo, setSidebarLogo] = useState(defaultSidebarLogo)
  const [fixedHeader, setFixedHeader] = useState(defaultFixedHeader)
  const [tagsView, setTagsView] = useState(defaultTagsView)

  const sidebarLogoChange = (checked) => {
    setSidebarLogo(checked)
    changeSetting({ key: 'sidebarLogo', value: checked })
  }

  const fixedHeaderChange = (checked) => {
    setFixedHeader(checked)
    changeSetting({ key: 'fixedHeader', value: checked })
  }

  const tagsViewChange = (checked) => {
    setTagsView(checked)
    changeSetting({ key: 'tagsView', value: checked })
  }

  const handleCopy = (e) => {
    let config = `
    export default {
      showSettings: true,
      sidebarLogo: ${sidebarLogo},
      fixedHeader: ${fixedHeader},
      tagsView: ${tagsView},
    }
    `
    clip(config, e)
  }

  return (
    <div className="rightSettings">
      <Drawer
        title="Pengaturan"
        placement="right"
        width={350}
        onClose={toggleSettingPanel}
        open={settingPanelVisible}
      >
        <Row>
          <Col span={12}>
            <span>Logo</span>
          </Col>
          <Col span={12}>
            <Switch
              checkedChildren="On"
              unCheckedChildren="Off"
              defaultChecked={sidebarLogo}
              onChange={sidebarLogoChange}
            />
          </Col>
        </Row>
        <Divider dashed />
        <Row>
          <Col span={12}>
            <span>Header</span>
          </Col>
          <Col span={12}>
            <Switch
              checkedChildren="On"
              unCheckedChildren="Off"
              defaultChecked={fixedHeader}
              onChange={fixedHeaderChange}
            />
          </Col>
        </Row>
        <Divider dashed />
        <Row>
          <Col span={12}>
            <span>Tags-View</span>
          </Col>
          <Col span={12}>
            <Switch
              checkedChildren="On"
              unCheckedChildren="Off"
              defaultChecked={tagsView}
              onChange={tagsViewChange}
            />
          </Col>
        </Row>
        <Divider dashed />
        <Row>
          <Col span={24}>
            <Alert
              message="Catatan untuk pengembang:"
              description="Bilah konfigurasi hanya digunakan untuk pratinjau di lingkungan pengembangan, dan tidak akan ditampilkan di lingkungan produksi. Harap salin dan ubah file konfigurasi /src/defaultSettings.js secara manual"
              type="warning"
              showIcon
              icon={<NotificationOutlined />}
              style={{ marginBottom: '16px' }}
            />
            <Button style={{ width: '100%' }} icon="copy" onClick={handleCopy}>
              Salin konfigurasi
            </Button>
          </Col>
        </Row>
      </Drawer>
    </div>
  )
}

const mapStateToProps = (state) => {
  return {
    ...state.app,
    ...state.settings,
  }
}

export default connect(mapStateToProps, { toggleSettingPanel, changeSetting })(
  RightPanel
)
