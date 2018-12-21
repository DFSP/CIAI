import * as React from 'react';
import { Fragment } from 'react';
import { IComment } from '../../../reducers/proposals';

const Comment = ({ id, title, text } : IComment) => (
  <Fragment>
    <li key={id}>
      <p><b>Title:</b> {title}</p>
      <p><b>Text:</b> {text}</p>
    </li>
    <br />
  </Fragment>
);

export default Comment
