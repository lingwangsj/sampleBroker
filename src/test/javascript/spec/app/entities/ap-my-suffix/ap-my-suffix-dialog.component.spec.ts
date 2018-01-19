/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleBrokerTestModule } from '../../../test.module';
import { APMySuffixDialogComponent } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix-dialog.component';
import { APMySuffixService } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix.service';
import { APMySuffix } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix.model';
import { WlanGroupMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix';
import { ZoneMySuffixService } from '../../../../../../main/webapp/app/entities/zone-my-suffix';

describe('Component Tests', () => {

    describe('APMySuffix Management Dialog Component', () => {
        let comp: APMySuffixDialogComponent;
        let fixture: ComponentFixture<APMySuffixDialogComponent>;
        let service: APMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [APMySuffixDialogComponent],
                providers: [
                    WlanGroupMySuffixService,
                    ZoneMySuffixService,
                    APMySuffixService
                ]
            })
            .overrideTemplate(APMySuffixDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(APMySuffixDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(APMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new APMySuffix(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.aP = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'aPListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new APMySuffix();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.aP = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'aPListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
