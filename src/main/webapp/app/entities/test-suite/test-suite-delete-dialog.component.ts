import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TestSuite } from './test-suite.model';
import { TestSuitePopupService } from './test-suite-popup.service';
import { TestSuiteService } from './test-suite.service';

@Component({
    selector: 'jhi-test-suite-delete-dialog',
    templateUrl: './test-suite-delete-dialog.component.html'
})
export class TestSuiteDeleteDialogComponent {

    testSuite: TestSuite;

    constructor(
        private testSuiteService: TestSuiteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.testSuiteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'testSuiteListModification',
                content: 'Deleted an testSuite'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-test-suite-delete-popup',
    template: ''
})
export class TestSuiteDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private testSuitePopupService: TestSuitePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.testSuitePopupService
                .open(TestSuiteDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
