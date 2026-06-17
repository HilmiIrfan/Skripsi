/* eslint-disable react/prop-types */
import { connect } from 'react-redux' // Use Redux to manage state
import TagList from './components/TagList'
import './index.less'

const TagsView = ({ sidebarCollapsed }) => {
  return (
    <div
      className="tagsView-container"
      style={{
        width: sidebarCollapsed ? 'calc(100% - 80px)' : 'calc(100% - 200px)',
        marginLeft: sidebarCollapsed ? '80px' : '200px',
      }}
    >
      <TagList />
    </div>
  )
}

// Map state to props
const mapStateToProps = (state) => ({
  sidebarCollapsed: state.app.sidebarCollapsed,
})

// Connect the component to Redux
export default connect(mapStateToProps)(TagsView)
