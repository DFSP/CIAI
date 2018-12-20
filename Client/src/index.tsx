import * as React from 'react';
import * as ReactDOM from 'react-dom';
import './index.css';
import registerServiceWorker from './registerServiceWorker';
import EcmaEvents from './views/ecmaEvents';

ReactDOM.render(
  <EcmaEvents />,
  document.getElementById('root') as HTMLElement
);
registerServiceWorker();
