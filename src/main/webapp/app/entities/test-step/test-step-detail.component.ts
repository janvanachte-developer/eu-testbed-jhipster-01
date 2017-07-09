import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TestStep } from './test-step.model';
import { TestStepService } from './test-step.service';

@Component({
    selector: 'jhi-test-step-detail',
    templateUrl: './test-step-detail.component.html'
})
export class TestStepDetailComponent implements OnInit, OnDestroy {

    testStep: TestStep;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private testStepService: TestStepService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTestSteps();
    }

    load(id) {
        this.testStepService.find(id).subscribe((testStep) => {
            this.testStep = testStep;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTestSteps() {
        this.eventSubscriber = this.eventManager.subscribe(
            'testStepListModification',
            (response) => this.load(this.testStep.id)
        );
    }
}
