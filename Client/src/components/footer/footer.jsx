import React from "react";
import classes from "./footer.css"
import { ListGroup, ListGroupItem } from 'react-bootstrap';

const Footer = () => (
    <footer className={classes.footer}>
        {console.log(classes)}
        <div className={classes.container}>
            <div className={classes.left}>
                <ListGroup className={classes.right}>
                    <ListGroupItem className={classes.inlineBlock}>
                        <a
                            href="https://bitbucket.org/dpimenta/ciai/src/master/Client"
                            target="_blank"
                            className={classes.block}
                        >
                            Repositório Cliente
                        </a>
                    </ListGroupItem>
                    <ListGroupItem className={classes.inlineBlock}>
                        <a
                            href="https://bitbucket.org/dpimenta/ciai/src/master/Server"
                            target="_blank"
                            className={classes.block}
                        >
                            Repositório Servidor
                        </a>
                    </ListGroupItem>
                    <ListGroupItem className={classes.inlineBlock}>
                        <a
                            href="" //TODO
                            target="_blank"
                            className={classes.block}
                        >
                            Servidor
                        </a>
                    </ListGroupItem>
                </ListGroup>
            </div>
            <p className={classes.right}>
          <span>
            &copy; {new Date().getFullYear()}
              {"  "} &bull; {" "}
              <a target="_blank" href="mailto:jc.reis@campus.fct.unl.pt">
              João Reis
            </a>
              {"  "} &bull; {" "}
              <a target="_blank" href="mailto:d.pimenta@campus.fct.unl.pt">
              Daniel Pimenta
            </a>
              {"  "} &bull; {" "}
              <a target="_blank" href="mailto:lg.martins@campus.fct.unl.pt">
              Luis Martins
            </a>
          </span>
            </p>
        </div>
    </footer>
);

export default Footer;
