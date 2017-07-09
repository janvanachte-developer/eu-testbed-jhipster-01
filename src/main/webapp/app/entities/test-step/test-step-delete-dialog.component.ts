import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TestStep } from './test-step.model';
import { TestStepPopupService } from './test-step-popup.service';
import { TestStepService } from './test-step.service';

@Component({
    selector: 'jhi-test-step-delete-dialog',
    templateUrl: './test-step-delete-dialog.component.html'
})
export class TestStepDeleteDialogComponent {

    testStep: TestStep;

    constructor(
        private testStepService: TestStepService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.testStepService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'testStepListModification',
                content: 'Deleted an testStep'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-test-step-delete-popup',
    template: ''
})
export class TestStepDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private testStepPopupService: TestStepPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.testStepPopupService
                .open(TestStepDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
