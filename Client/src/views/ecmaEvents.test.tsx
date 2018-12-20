import * as React from 'react';
import * as ReactDOM from 'react-dom';
import EcmaEvents from './ecmaEvents';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<EcmaEvents />, div);
  ReactDOM.unmountComponentAtNode(div);
});
