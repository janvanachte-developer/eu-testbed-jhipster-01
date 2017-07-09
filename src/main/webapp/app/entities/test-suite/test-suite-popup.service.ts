import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TestSuite } from './test-suite.model';
import { TestSuiteService } from './test-suite.service';

@Injectable()
export class TestSuitePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private testSuiteService: TestSuiteService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.testSuiteService.find(id).subscribe((testSuite) => {
                this.testSuiteModalRef(component, testSuite);
            });
        } else {
            return this.testSuiteModalRef(component, new TestSuite());
        }
    }

    testSuiteModalRef(component: Component, testSuite: TestSuite): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.testSuite = testSuite;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
