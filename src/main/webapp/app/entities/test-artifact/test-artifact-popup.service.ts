import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TestArtifact } from './test-artifact.model';
import { TestArtifactService } from './test-artifact.service';

@Injectable()
export class TestArtifactPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private testArtifactService: TestArtifactService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.testArtifactService.find(id).subscribe((testArtifact) => {
                this.testArtifactModalRef(component, testArtifact);
            });
        } else {
            return this.testArtifactModalRef(component, new TestArtifact());
        }
    }

    testArtifactModalRef(component: Component, testArtifact: TestArtifact): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.testArtifact = testArtifact;
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
