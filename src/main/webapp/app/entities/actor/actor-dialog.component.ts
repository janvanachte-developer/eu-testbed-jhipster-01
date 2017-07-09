import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Actor } from './actor.model';
import { ActorPopupService } from './actor-popup.service';
import { ActorService } from './actor.service';
import { TestSuite, TestSuiteService } from '../test-suite';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-actor-dialog',
    templateUrl: './actor-dialog.component.html'
})
export class ActorDialogComponent implements OnInit {

    actor: Actor;
    authorities: any[];
    isSaving: boolean;

    testsuites: TestSuite[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private actorService: ActorService,
        private testSuiteService: TestSuiteService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.testSuiteService.query()
            .subscribe((res: ResponseWrapper) => { this.testsuites = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.actor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.actorService.update(this.actor));
        } else {
            this.subscribeToSaveResponse(
                this.actorService.create(this.actor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Actor>) {
        result.subscribe((res: Actor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Actor) {
        this.eventManager.broadcast({ name: 'actorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackTestSuiteById(index: number, item: TestSuite) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-actor-popup',
    template: ''
})
export class ActorPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private actorPopupService: ActorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.actorPopupService
                    .open(ActorDialogComponent, params['id']);
            } else {
                this.modalRef = this.actorPopupService
                    .open(ActorDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
