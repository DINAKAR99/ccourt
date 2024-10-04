import React from 'react'
import PublicLayout from '../../Layouts/PublicLayout'

const Protected = () => {
  return (
    <PublicLayout>
    <div className="text-center "><br />
    <br />
      <h4>YOU ARE ACCESSING PROTECTED CONTENT </h4>
    </div>
    </PublicLayout>
  )
}

export default Protected