import logo from './logo.svg';
import './App.css';
import React, { Component } from 'react';

class FormerApp extends Component {
  state = {
    clients: []
  };

  async componentDidMount() {
    const response = await fetch('/clients');
    const body = await response.json();
    this.setState({clients: body});
  }

  render() {
    const {clients} = this.state;
    return (
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <div className="App-intro">
              <h2>Clients</h2>
              {clients.map(client =>
                  <div key={client.id}>
                    {client.name} ({client.email})
                  </div>
              )}
            </div>
          </header>
        </div>
    );
  }
}
export default FormerApp;
